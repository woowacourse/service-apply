import { useEffect, useRef, useState } from "react";
import { createPortal } from "react-dom";
import { ModalContextValue } from "../../../provider/ModalProvider";
import styles from "./ModalPortal.module.css";

type ModalPortalProps = React.PropsWithChildren<{
  closeModal: ModalContextValue["closeModal"];
}>;

const ModalPortal = ({ children, closeModal }: ModalPortalProps) => {
  const ref = useRef<Element | null>();
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    setMounted(true);

    const modalRoot = document.getElementById("modal-root");
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