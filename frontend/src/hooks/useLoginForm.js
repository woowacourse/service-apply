import { useState } from "react";

export const LOGIN_FORM = {
  EMAIL: "email",
  PASSWORD: "password",
};

const useLoginForm = () => {
  const [requiredForm, setRequiredForm] = useState({
    [LOGIN_FORM.EMAIL]: "",
    [LOGIN_FORM.PASSWORD]: "",
  });

  const isEmpty =
    Object.values(requiredForm).filter(Boolean).length <
    Object.keys(requiredForm).length;

  const handleEmailChange = ({ target }) => {
    setRequiredForm((prev) => ({
      ...prev,
      [LOGIN_FORM.EMAIL]: target.value,
    }));
  };

  const handlePasswordChange = ({ target }) => {
    setRequiredForm((prev) => ({
      ...prev,
      [LOGIN_FORM.PASSWORD]: target.value,
    }));
  };

  return {
    form: requiredForm,
    handleChange: {
      [LOGIN_FORM.EMAIL]: handleEmailChange,
      [LOGIN_FORM.PASSWORD]: handlePasswordChange,
    },
    isEmpty,
  };
};

export default useLoginForm;
