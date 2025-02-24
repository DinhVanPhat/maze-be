package com.maze.maze.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import com.maze.maze.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.maze.maze.dto.account.ChangePasswordDTO;
import com.maze.maze.dto.account.EditProfileDTO;
import com.maze.maze.dto.account.ForgotPasswordDTO;
import com.maze.maze.dto.account.RegisterDTO;
import com.maze.maze.dto.account.TokenRequest;
import com.maze.maze.model.Account;
import com.maze.maze.service.JWTService;
import com.maze.maze.service.MaHoaMKService;
import com.maze.maze.service.MailService;
import com.maze.maze.service.OptCodeService;
import com.maze.maze.service.PasswordEncoderService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("*")
@RestController
public class ApiAccount {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    MailService mailService;
    @Autowired
    OptCodeService optCodeService;
    @Autowired
    JWTService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoderService passwordEncoderService;

    @GetMapping("/api/account/list")
    public Map<String, Object> getAllTaiKhoan() {
        List<Account> list = accountRepository.findAll();

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("accounts", list));
        response.put("message", "Retrieve Account data successfully");
        return response;
    }

    // api/taikhoans?tenDangNhap=?&password=?
    @GetMapping("/api/get-account")
    public Map<String, Object> getTaiKhoanByTenDangNhapMatKhau(@RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("password") String password) {
        Account acc = accountRepository.findByPhoneNumber(phoneNumber);
        if (acc == null) {
            return response("error", acc, "Tên đăng nhập hoặc mật khẩu không đúng!!!");
        } else {
            System.out.println(passwordEncoderService.encodePassword(password));
            System.out.println("hi" + password.equals(acc.getPassword()));
            System.out.println(passwordEncoderService.matches(password, acc.getPassword()));
            if (password.equals(acc.getPassword()) || passwordEncoderService.matches(password, acc.getPassword())) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(acc.getPhoneNumber(),
                                password));
                if (authentication.isAuthenticated()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("data", Map.of("Account", acc));
                    String token = jwtService.GenerateToken(acc.getPhoneNumber(), map);
                    System.out.println("Generated Token: " + token); // Debugging log
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("token", token);
                    response.put("message", "Đăng nhập thành công");
                    return response;
                } else {
                    System.out.println("do lỗi nè");
                    throw new UsernameNotFoundException("Invalid user request..!!");

                }
            } else {
                return response("error", new Account(), "Tên đăng nhập hoặc mật khẩu không đúng!!!");
            }
        }

    }

    @PostMapping("/api/account/create")
    public Map<String, Object> postTaiKhoan(@RequestBody RegisterDTO registerDTO) {
        if (accountRepository.findByPhoneNumber(registerDTO.getPhoneNumber()) != null) {
            return response("error", new Account(), "Số điện thoại đã tồn tại!!!");
        } else if (accountRepository.findByEmail(registerDTO.getEmail()) != null) {
            return response("error", new Account(), "Email đã tồn tại!!!");
        } else {
            Account Account = new Account();
            Account.setEmail(registerDTO.getEmail());
            Account.setFullname(registerDTO.getFullname());
            Account.setPassword(MaHoaMKService.maHoaMatKhauMD5(registerDTO.getPassword()));
            Account.setPhoneNumber(registerDTO.getPhoneNumber());
            accountRepository.saveAndFlush(Account);
            return response("success", Account, "Đăng ký tài khoản thành công");
        }
    }

    @PutMapping("/api/account/edit-profile")
    public Map<String, Object> updateTaiKhoan(@RequestBody EditProfileDTO editProfileDTO) {
        Account accSdt = accountRepository.findByPhoneNumber(editProfileDTO.getPhoneNumber());
        if (!editProfileDTO.getEmail().equals(accSdt.getEmail())
                && accountRepository.findByEmail(editProfileDTO.getEmail()) != null) {
            return response("error", new Account(), "Email đã tồn tại!!!");
        }  else {
            accSdt.setFullname(editProfileDTO.getFullname());
            accSdt.setEmail(editProfileDTO.getEmail());
            accSdt.setPhoneNumber(editProfileDTO.getPhoneNumber());
            accSdt.setGender(editProfileDTO.getGender());
            accSdt.setAvatarUrl(editProfileDTO.getAvatarUrl());
            accountRepository.saveAndFlush(accSdt);
            return response("success", accSdt, "Cập nhật tài khoản thành công");
        }
    }

    @PutMapping("/api/account/change-password")
    public Map<String, Object> putMethodName(@RequestBody ChangePasswordDTO changePasswordDTO) {
        Account acc = accountRepository.findByPhoneNumber(changePasswordDTO.getPhoneNumber());
        if (!passwordEncoderService.matches(changePasswordDTO.getPassword(), acc.getPassword())) {
            return response("error", new Account(), "Mật khẩu cũ không chính xác!!!");
        } else {
            System.out.println(changePasswordDTO.getChangePassword());
            acc.setPassword(MaHoaMKService.maHoaMatKhauMD5(changePasswordDTO.getChangePassword()));
            accountRepository.saveAndFlush(acc);
            return response("success", acc, "Đổi mật khẩu thành công");
        }

    }

    @PostMapping("/api/account/forgot-password/send-otp")
    public Map<String, Object> sendOtp(@RequestParam("email") String email) {
        Map<String, Object> response = new HashMap<>();
        int code = optCodeService.generateCode();
        Account acc = accountRepository.findByEmail(email);
        if (acc == null) {
            return response("error", acc, "Email không tồn tại!!!");
        } else {
            if (mailService.sendOtp(email, "Forgot Password", "Your verification code OTP is " + code)) {
                response.put("status", "success");
                response.put("data",
                        Map.of("email", email, "code", MaHoaMKService.maHoaMatKhauMD5(String.valueOf(code))));
                response.put("message", "Đã gửi mã xác nhận");
                return response;
            } else {
                return response("error", new Account(), "Gửi mã xác nhận không thành công!!!");
            }

        }
    }

    @PostMapping("/api/account/forgot-password/change")
    public Map<String, Object> sendOtp(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        Integer code = optCodeService.getCode();
        if (code == null) {
            return response("errorr", null, "Mã xác nhận đã hết hạn");
        } else if (!code.equals(forgotPasswordDTO.getOtp())) {
            return response("error", null, "Mã xác nhận không đúng!!!");
        } else {
            Account acc = accountRepository.findByEmail(forgotPasswordDTO.getEmail());
            acc.setPassword(MaHoaMKService.maHoaMatKhauMD5(forgotPasswordDTO.getPassword()));
            accountRepository.saveAndFlush(acc);
            return response("success", acc, "Đổi mật khẩu thành công");
        }
    }

    @GetMapping("/api/lien-he")
    public Map<String, Object> guiLienHe(@RequestParam("fullname") String fullname, @RequestParam("email") String email,
            @RequestParam("tinNhan") String tinNhan) {
        Map<String, Object> response = new HashMap<>();
        if (mailService.sendOtp("tranvn7849@gmail.com", "Khách hàng: " + fullname + " liên hệ",
                "Nội dung: " + tinNhan + "\nEmail: " + email)) {
            response.put("status", "success");
            response.put("message", "Gửi thông tin liên hệ thành công");
            return response;
        } else {
            response.put("status", "error");
            response.put("message", "Gửi thông tin liên hệ thất bại");
            return response;
        }
    }

    public Map<String, Object> response(String status, Account Account, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("data", Account == null ? null : Map.of("Account", Account));
        response.put("message", message);
        return response;
    }

    // @PostMapping("api/account/auth/google")
    // public Map<String, Object> googleLogin(@RequestBody TokenRequest tokenRequest) {
    //     String tokenId = tokenRequest.getTokenId();
    //     try {
    //         // Thiết lập GoogleIdTokenVerifier
    //         GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
    //                 GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance())
    //                 .setAudience(Collections
    //                         .singletonList("68991665224-48e0o2dcf45bo7hgllage70huupt308l.apps.googleusercontent.com"))
    //                 .build();

    //         // Xác thực token
    //         GoogleIdToken idToken = verifier.verify(tokenId);
    //         if (idToken != null) {
    //             GoogleIdToken.Payload payload = idToken.getPayload();
                
    //             // Lấy thông tin người dùng từ payload
    //             String userId = payload.getSubject();
    //             String email = payload.getEmail();
    //             boolean emailVerified = payload.getEmailVerified();
    //             String name = (String) payload.get("name");
    //             String avatar = (String) payload.get("picture");

    //             Account Account = accountRepository.findByEmail(email);
    //             if (Account == null) {
    //                 int atIndex = email.indexOf('@');
    //                 String emailDaCat = atIndex != -1 ? email.substring(0, atIndex) : email;
    //                 Random random = new Random();
    //                 int newCode = 100000 + random.nextInt(900000);
    //                 String randomString = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

    //                 Account acc = new Account();
    //                 acc.setEmail(email);
    //                 acc.setFullname(name);
    //                 acc.setAvatarUrl(avatar);
    //                 acc.setPassword(MaHoaMKService.maHoaMatKhauMD5(randomString));
    //                 acc.setRole("user");
    //                 accountRepository.saveAndFlush(acc);

    //                 Map<String, Object> map = new HashMap<>();
    //                 map.put("data", Map.of("Account", acc));
    //                 // String token = jwtService.GenerateToken(acc.getTenDangNhap(), map);
    //                 // System.out.println("Generated Token: " + token); // Debugging log
    //                 Map<String, Object> response = new HashMap<>();
    //                 response.put("status", "success");
    //                 // response.put("token", token);
    //                 response.put("message", "Đăng nhập thành công");
    //                 return response;
    //             } else {
    //                 Map<String, Object> map = new HashMap<>();
    //                 map.put("data", Map.of("Account", Account));
    //                 String token = jwtService.GenerateToken(Account.getTenDangNhap(), map);
    //                 System.out.println("Generated Token: " + token); // Debugging log
    //                 Map<String, Object> response = new HashMap<>();
    //                 response.put("status", "success");
    //                 response.put("token", token);
    //                 response.put("message", "Đăng nhập thành công");
    //                 return response;
    //             }
    //             // // Tiếp tục xử lý, như tạo session hoặc JWT token cho người dùng
    //             // Map<String, Object> response = new HashMap<>();
    //             // response.put("status", "success");
    //             // response.put("data", Map.of("userId", userId, "email", email, "name", name,
    //             // "emailVerified", emailVerified, "payload", payload,"emailDaCat",
    //             // emailDaCat+newCode, "randomString", randomString));
    //             // response.put("message", "message");
    //             // return response;
    //         } else {
    //             return null;
    //         }

    //     } catch (Exception e) {
    //         Map<String, Object> response = new HashMap<>();
    //         response.put("status", "error");
    //         response.put("data", null);
    //         response.put("message", e.getMessage());
    //         return response;

    //     }
    // }
}
