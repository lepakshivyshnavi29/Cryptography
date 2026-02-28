package com.example.cryptography.controller;

import com.example.cryptography.entity.Transaction;
import com.example.cryptography.entity.User;
import com.example.cryptography.service.TransactionService;
import com.example.cryptography.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class CryptographyController {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        try {
            User user = userService.loginUser(username, password);
            session.setAttribute("user", user);
            return "redirect:/node";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password,HttpSession session,RedirectAttributes redirectAttributes) {
        try {
            User user = userService.registerUser(username, password);
            session.setAttribute("user", user);
            redirectAttributes.addFlashAttribute("success",
                    "Registration successful! Your Node ID: " + user.getNodeId());
            return "redirect:/node";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/node")
    public String nodePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "node";
    }

    @GetMapping("/token")
    public String tokenPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "token";
    }

    @GetMapping("/transfer")
    public String transferPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);

        // Get received transactions for this user
        List<Transaction> receivedTransactions =
                transactionService.getReceivedTransactionsForNode(user.getNodeId());
        model.addAttribute("receivedTransactions", receivedTransactions);

        return "transfer";
    }

    @PostMapping("/transfer")
    public String processTransfer(@RequestParam String text, @RequestParam String key,@RequestParam String destinationNode, HttpSession session,RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Validate destination node exists
        Optional<User> destinationUser = userService.findByNodeId(destinationNode);
        if (destinationUser.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Destination node not found!");
            return "redirect:/transfer";
        }

        // Create transaction
        Transaction transaction = transactionService.createTransaction(
                user.getNodeId(), destinationNode, text, key);

        redirectAttributes.addFlashAttribute("success",
                "Message transferred successfully to " + destinationNode +
                        " using " + transaction.getCipherType() + " cipher!");
        return "redirect:/transfer";
    }

    @GetMapping("/ledger")
    public String ledgerPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Transaction> transactions =
                transactionService.getAllTransactionsForNode(user.getNodeId());
        model.addAttribute("user", user);
        model.addAttribute("transactions", transactions);
        return "ledger";
    }

    @PostMapping("/decrypt")
    public String decryptMessage(@RequestParam Long transactionId, @RequestParam String key,HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Transaction> receivedTransactions =
                transactionService.getReceivedTransactionsForNode(user.getNodeId());
        Transaction transaction = receivedTransactions.stream()
                .filter(t -> t.getId().equals(transactionId))
                .findFirst()
                .orElse(null);

        if (transaction == null) {
            redirectAttributes.addFlashAttribute("error", "Transaction not found!");
            return "redirect:/transfer";
        }

        String decryptedText = transactionService.decryptTransaction(transaction, key);

        model.addAttribute("user", user);
        model.addAttribute("receivedTransactions", receivedTransactions);
        model.addAttribute("decryptedText", decryptedText);
        model.addAttribute("decryptedTransaction", transaction);

        return "transfer";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
