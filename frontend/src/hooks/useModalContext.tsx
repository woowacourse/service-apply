import React, { useContext, createContext } from "react";
import { ERROR_MESSAGE } from "../constants/messages";

export type ModalContextValue = {
  Modal: React.FC<{ children: NonNullable<React.ReactNode> }>;
  openModal: () => void;
  closeModal: () => void;
};

export const ModalContext = createContext<ModalContextValue | null>(null);

const useModalContext = () => {
  const modalContext = useContext(ModalContext);

  if (!modalContext) throw Error(ERROR_MESSAGE.HOOKS.CANNOT_FIND_MODAL_CONTEXT);

  return modalContext;
};

export default useModalContext;
