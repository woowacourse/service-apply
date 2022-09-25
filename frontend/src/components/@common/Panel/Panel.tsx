import { useState } from "react";

import styles from "./Panel.module.css";
import Container from "../Container/Container";
import IconButton from "../IconButton/IconButton";

export type PanelProps = {
  isOpen?: boolean;
  className?: string;
  title: string;
  children: React.ReactNode;
};

const Panel = ({ isOpen = false, className, title, children }: PanelProps) => {
  const [isPanelOpen, setIsPanelOpen] = useState(isOpen);

  const toggleIsPanelOpen = () => {
    setIsPanelOpen((prev) => !prev);
  };

  return (
    <div className={className}>
      <Container className={styles["panel-header"]} onClick={toggleIsPanelOpen}>
        <h3 className={styles.title}>{title}</h3>
        <IconButton
          src={isPanelOpen ? "/assets/icon/arrow-up.svg" : "/assets/icon/arrow-down.svg"}
          aria-label={isPanelOpen ? "패널 닫기" : "패널 열기"}
        />
      </Container>
      {isPanelOpen && <Container className={styles["panel-body"]}>{children}</Container>}
    </div>
  );
};

export default Panel;
