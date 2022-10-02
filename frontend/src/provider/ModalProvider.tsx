import { useState } from "react";
import ModalPortal from "../components/Modal/ModalPortal/ModalPortal";
import { ModalContext } from "../hooks/useModalContext";

const ModalProvider = ({ children }: { children: React.ReactNode }) => {
  const [isModalOpened, setIsModalOpened] = useState(false);

  const openModal = () => {
    document.body.style.overflow = "hidden";
    setIsModalOpened(true);
  };

  const closeModal = () => {
    document.body.style.overflow = "auto";
    setIsModalOpened(false);
  };

  const Modal: React.FC<{ children: NonNullable<React.ReactNode> }> = (props) => {
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
