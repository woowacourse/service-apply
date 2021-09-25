import React, { useState, useEffect } from "react";
import { Link, useHistory, useLocation } from "react-router-dom";
import classNames from "classnames";
import Box from "../../components/@common/Box/Box";
import RecruitItem from "../../components/RecruitItem/RecruitItem";
import ApplicationFormItem from "../../components/ApplicationFormItem/ApplicationFormItem";
import useTokenContext from "../../hooks/useTokenContext";
import useRecruitmentContext from "../../hooks/useRecruitmentContext";
import { RECRUITS_TAB, RECRUITS_TAB_LIST } from "../../constants/tab";
import { ERROR_MESSAGE } from "../../constants/messages";
import PATH from "../../constants/path";
import styles from "./Recruits.module.css";
import { generateQuery } from "../../utils/route/query";

const Recruits = () => {
  const query = new URLSearchParams(useLocation().search);
  const selectedTab = query.get("status") ?? RECRUITS_TAB.ALL.name;

  const [myApplication, setMyApplication] = useState([]);

  const { recruitment, fetchMyApplicationForms } = useRecruitmentContext();
  const { token } = useTokenContext();

  const history = useHistory();

  useEffect(() => {
    (async () => {
      if (selectedTab !== RECRUITS_TAB.APPLIED.name) {
        return;
      }

      if (token === "") {
        alert(ERROR_MESSAGE.ACCESS.REQUIRED_LOGIN);
        history.push(PATH.LOGIN);

        return;
      }

      try {
        const myApplicationFormData = await fetchMyApplicationForms(token);
        const myRecruits = myApplicationFormData.map(
          ({ recruitmentId, submitted }) => ({
            ...recruitment.findById(recruitmentId),
            submitted,
          })
        );

        setMyApplication(myRecruits);
      } catch (e) {
        console.error(e);

        alert(ERROR_MESSAGE.API.FETCHING_MY_APPLICATION);
        history.push(PATH.LOGIN);
      }
    })();
  }, [selectedTab, recruitment, fetchMyApplicationForms, token, history]);

  return (
    <div className={styles.recruits}>
      <Box>
        <ul className={styles["tab-list"]}>
          {RECRUITS_TAB_LIST.map(({ name, label }) => (
            <li key={name} className={styles["tab-item"]}>
              <Link
                to={{
                  pathname: PATH.RECRUITS,
                  search: generateQuery({ status: name }),
                }}
                className={classNames({
                  [styles.active]: name === selectedTab,
                })}
              >
                {label}
              </Link>
            </li>
          ))}
          {token && (
            <li
              className={classNames(
                styles["tab-item"],
                styles["edit-password"]
              )}
            >
              <Link to={PATH.EDIT_PASSWORD}>비밀번호 변경</Link>
            </li>
          )}
        </ul>
        {selectedTab === RECRUITS_TAB.APPLIED.name
          ? myApplication.length !== 0 &&
            myApplication.map((recruitment) => (
              <ApplicationFormItem
                className={styles["application-forms"]}
                key={recruitment.id}
                recruitment={recruitment}
                submitted={recruitment.submitted}
              />
            ))
          : recruitment[selectedTab].map((recruitment) => (
              <RecruitItem key={recruitment.id} recruitment={recruitment} />
            ))}
      </Box>
    </div>
  );
};

export default Recruits;
