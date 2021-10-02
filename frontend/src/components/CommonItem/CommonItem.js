import React, { useMemo } from "react";
import PropTypes from "prop-types";
import classNames from "classnames";

import { formatDateTime } from "../../utils/format/date";

import BaseItem from "../BaseItem/BaseItem";
import Button from "../@common/Button/Button";

import styles from "./CommonItem.module.css";

const CommonItem = ({
  buttonLabel,
  activeButton,
  recruitment,
  goPage,
  className,
}) => {
  const formattedStartDateTime = useMemo(
    () =>
      recruitment.startDateTime
        ? formatDateTime(new Date(recruitment.startDateTime))
        : "",
    [recruitment.startDateTime]
  );

  const formattedEndDateTime = useMemo(
    () =>
      recruitment.endDateTime
        ? formatDateTime(new Date(recruitment.endDateTime))
        : "",
    [recruitment.endDateTime]
  );

  return (
    <div className={classNames(styles["common-item"], className)}>
      <BaseItem
        title={recruitment.title}
        startDateTime={formattedStartDateTime}
        endDateTime={formattedEndDateTime}
      />
      <Button
        className="button"
        type="button"
        disabled={!activeButton}
        onClick={goPage}
      >
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
  className: PropTypes.string,
};
