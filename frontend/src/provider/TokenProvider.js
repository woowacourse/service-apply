import React, { useState } from "react";
import { useHistory } from "react-router";

import { TokenContext } from "../hooks/useTokenContext";
import * as Api from "../api";

const TokenProvider = ({ children }) => {
  const [token, setToken] = useState("");
  const history = useHistory();

  const postRegister = async (payload) => {
    try {
      const { data: token } = await Api.fetchRegister(payload);

      setToken(token);
    } catch (e) {
      alert("이미 신청서를 작성했습니다. 로그인 페이지로 이동합니다.");
      history.push("/login");
    }
  };

  const fetchLogin = async (payload) => {
    try {
      const { data: token } = await Api.fetchLogin(payload);

      setToken(token);
    } catch (e) {
      alert(e.response.data.message);
    }
  };

  const resetToken = () => {
    setToken("");
  };

  return (
    <TokenContext.Provider
      value={{ token, postRegister, fetchLogin, resetToken }}
    >
      {children}
    </TokenContext.Provider>
  );
};

export default TokenProvider;
