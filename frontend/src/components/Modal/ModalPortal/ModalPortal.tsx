import { useEffect, useRef, useState } from "react";
import { createPortal } from "react-dom";
import { ModalContextValue } from "../../../provider/ModalProvider";
import styles from "./ModalPortal.module.css";

type ModalPortalProps = {
  closeModal: ModalContextValue["closeModal"];
  children: NonNullable<React.ReactNode>;
};

const ModalPortal = ({ children, closeModal }: ModalPortalProps) => {
  const ref = useRef<Element | null>(null);
  const [mounted, setMounted] = useState(false);

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
      <div
        className={styles.box}
        onKeyDown={(e) => {
          if (e.key === "Escape") closeModal();
        }}
      >
        <div className={styles["dimmer-box"]} onClick={closeModal} />
        {children}
      </div>,
      ref.current
    );
  }

  return null;
};
export default ModalPortal;
