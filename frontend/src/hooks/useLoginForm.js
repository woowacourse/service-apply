import { useState } from "react";

const useLoginForm = () => {
  const [requiredForm, setRequiredForm] = useState({ email: "", password: "" });

  const isEmpty =
    Object.values(requiredForm).filter(Boolean).length <
    Object.keys(requiredForm).length;

  const handleFormChange = ({ target }) => {
    setRequiredForm((prev) => ({
      ...prev,
      [target.name]: target.value,
    }));
  };

  return {
    form: requiredForm,
    handleFormChange,
    isEmpty,
  };
};

export default useLoginForm;
