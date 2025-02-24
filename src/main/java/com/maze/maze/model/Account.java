package com.maze.maze.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import com.maze.maze.groupValidation.DangNhapGroup;
import com.maze.maze.groupValidation.FullValidationTaiKhoanGroup;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import java.time.LocalDateTime;
import jakarta.persistence.EnumType;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {

  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(
            message = "Vui lòng nhập tên đăng nhập",
            groups = { DangNhapGroup.class, FullValidationTaiKhoanGroup.class }
    )
    private String fullname;

    @NotEmpty(
            message = "Vui lòng nhập mật khẩu",
            groups = { DangNhapGroup.class, FullValidationTaiKhoanGroup.class }
    )
    @Size(
            min = 8,
            message = "Mật khẩu phải có tối thiểu 8 ký tự",
            groups = { DangNhapGroup.class, FullValidationTaiKhoanGroup.class }
    )
    @JsonIgnore
    private String password;

    @NotEmpty(
            message = "Vui lòng nhập email",
            groups = { FullValidationTaiKhoanGroup.class }
    )
    @Email(
            message = "Vui lòng nhập đúng định dạng email",
            groups = { FullValidationTaiKhoanGroup.class }
    )
    private String email;

    Date birthDate;

    boolean gender = true;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('user', 'seller', 'admin') DEFAULT 'user'")
    private Role role = Role.USER;

    private String avatarUrl;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum Role {
        USER, SELLER, ADMIN
    }

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}
