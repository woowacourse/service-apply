import { useLocation, useNavigate } from "react-router-dom";

import Container, { CONTAINER_SIZE } from "../../components/@common/Container/Container";
import Button from "../../components/@common/Button/Button";

import { PATH } from "../../constants/path";
import styles from "./PasswordFindResult.module.css";

const PasswordFindResult = () => {
  const {
    state: { email },
  } = useLocation();

  const navigate = useNavigate();

  return (
    <Container className={styles["flex-col"]} size={CONTAINER_SIZE.NARROW} title="비밀번호 찾기">
      <p className={styles.text}>
        임시 비밀번호가 <strong>{email}</strong> 으로 발송되었습니다.
      </p>
      <Button type="button" onClick={() => navigate(PATH.HOME)}>
        돌아가기
      </Button>
    </Container>
  );
};

export default PasswordFindResult;
