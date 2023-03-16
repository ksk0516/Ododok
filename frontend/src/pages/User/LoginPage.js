import React, { useState } from "react";
import TextField from "@mui/material/TextField";
import styles from "../../styles/Login.module.scss";
import Grid from "@mui/material/Grid"; // Grid version 1
import Button from "@mui/material/Button";
import axios from 'axios';
import { useNavigate } from "react-router-dom";

function LoginPage() {
  const movePage = useNavigate();
  const [form, setForm] = useState({
    email: "",
    password: "",
  });

  const goSignup = () => {
    movePage("/signup");
  }

  const userLogin = () => {
    axios({
      methods: 'post',
      url: '#',
      data: form,
    })
      .then((res) => {
        console.log(res)
      })
      .catch((err) => {
        console.log(err)
      })
  }
  return (
    <Grid
      container
      direction="column"
      justifyContent="center"
      alignItems="center"
    >
      <h2>Oh Do Dok!</h2>
      <div className={styles["loginBox"]}>
        <h3 className={styles["title"]}>로그인</h3>
        <Grid container direction="row" columnGap={10}>
          <p className={styles["login-blank"]}>이메일</p>
          <TextField
            required
            id="email"
            label="Required"
            placeholder="이메일을 입력해주세요"
            value={form.email}
            variant="standard"
            onChange={(e) => setForm({ ...form, email: e.target.value })}
          />
        </Grid>
        <Grid container direction="row" columnGap={8}>
          <p className={styles["login-blank"]}>비밀번호</p>
          <TextField
            required
            id="password"
            label="Required"
            placeholder="비밀번호를 입력해주세요"
            value={form.password}
            variant="standard"
            onChange={(e) => setForm({ ...form, password: e.target.value })}
          />
        </Grid>
        <Grid container direction="row" justifyContent={"space-between"}>
          <p className={styles["small"]}>아이디/비밀번호 찾기</p>
          <p className={styles["small"]} onClick={goSignup}>가입하기</p>
        </Grid>
        <hr />
        <br />
        <Grid
          container
          direction="row"
          justifyContent="center"
          alignItems="center"
          columnGap={2}
        >
          <Button variant="contained">취소</Button>
          <Button variant="contained" color="success" onClick={userLogin}>
            로그인
          </Button>
        </Grid>
      </div>
    </Grid>
  );
}

export default LoginPage;