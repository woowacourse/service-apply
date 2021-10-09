import { useState } from "react";
import PropTypes from "prop-types";

import styles from "./Panel.module.css";
import Container, { CONTAINER_SIZE } from "../Container/Container";
import IconButton from "../IconButton/IconButton";
import classNames from "../../../../node_modules/classnames/index";

const Panel = ({ title, children, className }) => {
  const [isPanelOpen, setIsPanelOpen] = useState(false);

  const toggleIsPanelOpen = () => {
    setIsPanelOpen((prev) => !prev);
  };

  return (
    <>
      <Container className={styles["panel-header"]}>
        <h3>{title}</h3>
        <IconButton
          src={
            isPanelOpen
              ? "/assets/icon/arrow-up.svg"
              : "/assets/icon/arrow-down.svg"
          }
          aria-label={isPanelOpen ? "패널 닫기" : "패널 열기"}
          onClick={toggleIsPanelOpen}
        />
      </Container>
      {isPanelOpen && (
        <Container className={classNames(styles["panel-body"], className)}>
          {children}
        </Container>
      )}
    </>
  );
};

Panel.propTypes = {
  title: PropTypes.string,
};

export default Panel;
