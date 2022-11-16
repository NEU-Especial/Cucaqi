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
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String gender;
    private String age;

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                ", job='" + job + '\'' +
                ", birth=" + birth +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", telephone='" + telephone + '\'' +
                ", securityQuestion=" + securityQuestion +
                ", securityAnswer='" + securityAnswer + '\'' +
                ", role=" + role +
                ", payment=" + payment +
                ", limitCount=" + limitCount +
                ", curCount=" + curCount +
                ", deleted=" + deleted +
                ", createdBy=" + createdBy +
                ", inviteCode='" + inviteCode + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public User(Integer id, String gender, String age, String job, Date birth, String username, String password, String telephone, Integer securityQuestion, String securityAnswer, Integer role, Float payment, Integer limitCount, Integer curCount, Integer deleted, Integer createdBy, String inviteCode, String email) {
        this.id = id;
        this.gender = gender;
        this.age = age;
        this.job = job;
        this.birth = birth;
        this.username = username;
        this.password = password;
        this.telephone = telephone;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.role = role;
        this.payment = payment;
        this.limitCount = limitCount;
        this.curCount = curCount;
        this.deleted = deleted;
        this.createdBy = createdBy;
        this.inviteCode = inviteCode;
        this.email = email;
    }

    private String job;

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

    private Date birth;

    private String username;

    private String password;

    private String telephone;

    private Integer securityQuestion;

    private String securityAnswer;

    private Integer role;

    private Float payment;


    private Integer limitCount;

    public Integer getCurCount() {
        return curCount;
    }

    public void setCurCount(Integer curCount) {
        this.curCount = curCount;
    }

    private Integer curCount;
    @TableLogic
    private Integer deleted;

    private Integer createdBy;


    public Integer getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(Integer limitCount) {
        this.limitCount = limitCount;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    private String inviteCode;


    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
