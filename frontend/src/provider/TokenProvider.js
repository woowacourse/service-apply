import React, { useState } from "react";
import { TokenContext } from "../hooks/useTokenContext";
import * as Api from "../api";

const TokenProvider = ({ children }) => {
  const [token, setToken] = useState("");

  const postRegister = async (payload) => {
    const { data: token } = await Api.fetchRegister(payload);
    console.log(token);
    setToken(token);
  };

  const fetchLogin = async (payload) => {
    const { data: token } = await Api.fetchLogin(payload);

    setToken(token);
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
