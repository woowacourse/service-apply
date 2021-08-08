import React, { useMemo } from "react";
import PropTypes from "prop-types";

import BaseItem from "../BaseItem/BaseItem";

import { Button } from "../form";

import styles from "./CommonItem.module.css";
import { formatDateTime } from "../../utils/date";

const CommonItem = ({ buttonLabel, activeButton, recruitment, goPage }) => {
  const formattedStartDateTime = useMemo(
    () => formatDateTime(new Date(recruitment.startDateTime)),
    [recruitment.startDateTime]
  );

  const formattedEndDateTime = useMemo(
    () => formatDateTime(new Date(recruitment.endDateTime)),
    [recruitment.endDateTime]
  );

  return (
    <div className={styles["common-item"]}>
      <BaseItem
        title={recruitment.title}
        startDateTime={formattedStartDateTime}
        endDateTime={formattedEndDateTime}
      />
      <Button className="button" disabled={!activeButton} onClick={goPage}>
        {buttonLabel}
      </Button>
    </div>
  );
};

export default CommonItem;

CommonItem.propTypes = {
  buttonLabel: PropTypes.string.isRequired,
  activeButton: PropTypes.bool.isRequired,
  goPage: PropTypes.func.isRequired,
  recruitment: PropTypes.object.isRequired,
};
