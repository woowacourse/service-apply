import { useContext, createContext } from "react";
import { ERROR_MESSAGE } from "../constants/messages";
import { ModalContextValue } from "../provider/ModalProvider";

export const ModalContext = createContext<ModalContextValue | null>(null);

const useModalContext = () => {
  const modalContext = useContext(ModalContext);

  if (!modalContext) throw Error(ERROR_MESSAGE.HOOKS.CANNOT_FIND_MODAL_CONTEXT);

  return modalContext;
};

export default useModalContext;
