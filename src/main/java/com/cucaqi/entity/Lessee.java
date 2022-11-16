package com.cucaqi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author GaoSong Xu
 * @since 2022-10-31
 */
@TableName("t_lessee")
public class Lessee implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

    public Lessee() {
    }

    @Override
    public String toString() {
        return "Lessee{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                ", job='" + job + '\'' +
                ", birth=" + birth +
                ", password='" + password + '\'' +
                ", telephone='" + telephone + '\'' +
                ", securityQuestion=" + securityQuestion +
                ", securityAnswer='" + securityAnswer + '\'' +
                ", role=" + role +
                ", payment=" + payment +
                ", deleted=" + deleted +
                ", createdBy=" + createdBy +
                ", InviteCode='" + InviteCode + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public Lessee(Integer id, String username, String gender, String age, String job, Date birth, String password, String telephone, Integer securityQuestion, String securityAnswer, Integer role, Float payment, Integer deleted, Integer createdBy, String inviteCode, String email) {
        this.id = id;
        this.username = username;
        this.gender = gender;
        this.age = age;
        this.job = job;
        this.birth = birth;
        this.password = password;
        this.telephone = telephone;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.role = role;
        this.payment = payment;
        this.deleted = deleted;
        this.createdBy = createdBy;
        InviteCode = inviteCode;
        this.email = email;
    }

    private String gender;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    private String age;
    private String job;
    private Date birth;

    private String password;

    private String telephone;

    private Integer securityQuestion;

    private String securityAnswer;

    private Integer role;

    private Float payment;
    @TableLogic
    private Integer deleted;

    private Integer createdBy;

    private String InviteCode;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getInviteCode() {
        return InviteCode;
    }

    public void setInviteCode(String inviteCode) {
        InviteCode = inviteCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(Integer securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Float getPayment() {
        return payment;
    }

    public void setPayment(Float payment) {
        this.payment = payment;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

}
