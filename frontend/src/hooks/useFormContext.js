import { createContext, useContext } from "react";
import { ERROR_MESSAGE } from "../constants/messages";

export const FormContext = createContext(null);

const useFormContext = () => {
  const context = useContext(FormContext);

  if (!context) throw new Error(ERROR_MESSAGE.HOOKS.CANNOT_FIND_FORM_CONTEXT);

  return context;
};

export default useFormContext;
