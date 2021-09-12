import { createContext, useContext } from "react";

export const FormContext = createContext(null);

const useFormContext = () => {
  const context = useContext(FormContext);

  if (!context) throw new Error("FormContext가 존재하지 않습니다.");

  return context;
};

export default useFormContext;
