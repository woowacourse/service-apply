import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";

import BaseItem from "../BaseItem/BaseItem";
import Box from "../Box/Box";

import styles from "./RecruitCard.module.css";

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
