import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";

import BaseItem from "../BaseItem/BaseItem";
import Box from "../Box/Box";

import { formatDateTime } from "../../utils/date";

import styles from "./RecruitCard.module.css";

const RecruitCard = ({ title, startDateTime, endDateTime, className }) => {
  const formattedStartDateTime = formatDateTime(new Date(startDateTime));
  const formattedEndDateTime = formatDateTime(new Date(endDateTime));

  return (
    <Box className={classNames(styles["recruit-card"], className)}>
      <BaseItem
        title={title}
        startDateTime={formattedStartDateTime}
        endDateTime={formattedEndDateTime}
      />
    </Box>
  );
};

export default RecruitCard;

RecruitCard.propTypes = {
  title: PropTypes.string.isRequired,
  startDateTime: PropTypes.string.isRequired,
  endDateTime: PropTypes.string.isRequired,
};
