import React, { useState } from "react";
import ModalPortal from "../components/Modal/ModalPortal/ModalPortal";
import { ModalContext } from "../hooks/useModalContext";

export type ModalContextValue = {
  Modal: (props: { children: NonNullable<React.ReactNode> }) => JSX.Element | null;
  openModal: () => void;
  closeModal: () => void;
};

type ModalProviderProps = { children: React.ReactNode };

const ModalProvider = ({ children }: ModalProviderProps) => {
  const [isModalOpened, setIsModalOpened] = useState(false);

  const openModal: ModalContextValue["openModal"] = () => {
    document.body.style.overflow = "hidden";
    setIsModalOpened(true);
  };

  const closeModal: ModalContextValue["closeModal"] = () => {
    document.body.style.overflow = "auto";
    setIsModalOpened(false);
  };

  const Modal: ModalContextValue["Modal"] = (props) => {
    if (isModalOpened) {
      return <ModalPortal closeModal={closeModal} {...props} />;
    }

    return null;
  };

  return (
    <ModalContext.Provider value={{ Modal, openModal, closeModal }}>
      {children}
    </ModalContext.Provider>
  );
};

export default ModalProvider;
