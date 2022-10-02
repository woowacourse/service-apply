import React, { useContext, createContext } from "react";
import { ERROR_MESSAGE } from "../constants/messages";

export const ModalContext = createContext<{
  Modal: React.FC<{ children: NonNullable<React.ReactNode> }>;
  openModal: () => void;
  closeModal: () => void;
} | null>(null);

const useModalContext = () => {
  const modalContext = useContext(ModalContext);

  if (!modalContext) throw Error(ERROR_MESSAGE.HOOKS.CANNOT_FIND_MODAL_CONTEXT);

  return modalContext;
};

export default useModalContext;
