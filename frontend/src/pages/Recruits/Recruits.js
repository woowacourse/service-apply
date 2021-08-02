import React, { useState, useEffect } from "react";
import { Link, useHistory, useLocation } from "react-router-dom";
import classNames from "classnames";
import Box from "../../components/Box/Box";
import RecruitItem from "../../components/RecruitItem/RecruitItem";
import ApplicationFormItem from "../../components/ApplicationFormItem/ApplicationFormItem";
import useTokenContext from "../../hooks/useTokenContext";
import useRecruitmentContext from "../../hooks/useRecruitmentContext";
import { TAB_LIST } from "../../constants/tab";
import styles from "./Recruits.module.css";

const Recruits = () => {
  const query = new URLSearchParams(useLocation().search);
  const selectedTab = query.get("status") ?? "all";

  const [myApplication, setMyApplication] = useState([]);

  const { recruitment, fetchMyApplicationForms } = useRecruitmentContext();
  const { token } = useTokenContext();

  const history = useHistory();

  useEffect(() => {
    (async () => {
      if (selectedTab !== "applied") {
        return;
      }

      if (token === "") {
        alert("로그인이 필요합니다.");
        history.push("/login");

        return;
      }

      try {
        const data = await fetchMyApplicationForms(token);

        setMyApplication(data);
      } catch (e) {
        alert("내 지원서를 불러오는데 실패했습니다.");

        history.push("/login");
      }
    })();
  }, [selectedTab, fetchMyApplicationForms, token, history]);

  return (
    <div className={styles.recruits}>
      <Box>
        <ul className={styles["tab-list"]}>
          {TAB_LIST.map(({ name, label }) => (
            <li key={name} className={styles["tab-item"]}>
              <Link
                to={`/recruits?status=${name}`}
                className={classNames({ active: name === selectedTab })}
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
              <Link to="/edit">비밀번호 변경</Link>
            </li>
          )}
        </ul>
        {selectedTab === "applied"
          ? myApplication.map((recruitment) => (
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
