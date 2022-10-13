import { useEffect, useRef, useState } from "react";
import { createPortal } from "react-dom";
import { ModalContextValue } from "../../../hooks/useModalContext";
import styles from "./ModalPortal.module.css";

type ModalPortalProps = {
  closeModal: ModalContextValue["closeModal"];
  children: NonNullable<React.ReactNode>;
};

const ModalPortal = ({ children, closeModal }: ModalPortalProps) => {
  const ref = useRef<Element | null>(null);
  const [mounted, setMounted] = useState(false);

  const handleKeyDown: React.KeyboardEventHandler<HTMLDivElement> = (e) => {
    if (e.key === "Escape") {
      closeModal();
    }
  };

  useEffect(() => {
    setMounted(true);

    let modalRoot = document.getElementById("modal-root");

    if (!modalRoot) {
      modalRoot = document.createElement("div");
      modalRoot.id = "modal-root";
      document.body.appendChild(modalRoot);
    }

    ref.current = modalRoot;
  }, []);

  if (ref.current && mounted) {
    return createPortal(
      <div className={styles.box} onKeyDown={handleKeyDown}>
        <div className={styles["dimmer-box"]} onClick={closeModal} />
        {children}
      </div>,
      ref.current
    );
  }

  return null;
};
export default ModalPortal;
