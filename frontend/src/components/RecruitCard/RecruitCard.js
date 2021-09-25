import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";

import BaseItem from "../BaseItem/BaseItem";

import styles from "./RecruitCard.module.css";
import Box from "../@common/Box/Box";

const RecruitCard = ({ title, startDateTime, endDateTime, className }) => {
  return (
    <Box className={classNames(styles["recruit-card"], className)}>
      <BaseItem
        title={title}
        startDateTime={startDateTime}
        endDateTime={endDateTime}
      />
    </Box>
  );
};

export default RecruitCard;

RecruitCard.propTypes = {
  title: PropTypes.string,
  startDateTime: PropTypes.string,
  endDateTime: PropTypes.string,
};

RecruitCard.defaultProps = {
  title: "",
  startDateTime: "",
  endDateTime: "",
};
