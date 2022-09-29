import { RiKakaoTalkFill } from "react-icons/ri";
import styles from "./InquiryFloatingButton.module.css";
import { INQUIRE_LINK_URL } from "../../constants/url";

const InquiryFloatingButton = () => {
  return (
    <a
      href={INQUIRE_LINK_URL}
      target="_blank"
      rel="noreferrer"
      className={styles["floating-wrapper"]}
    >
      <p className={styles["inquire-text"]}>문의하기</p>
      <div className={styles["kakao-icon-box"]}>
        <RiKakaoTalkFill color="#fff" size={30} />
      </div>
    </a>
  );
};

export default InquiryFloatingButton;
