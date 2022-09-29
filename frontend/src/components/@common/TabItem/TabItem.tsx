import { PropsWithChildren } from "react";
import classNames from "classnames";
import styles from "./TabItem.module.css";

type TabItemProps = {
  checked: boolean;
  onClickTabItem: () => void;
};

const TabItem = ({ children, checked, onClickTabItem }: PropsWithChildren<TabItemProps>) => {
  return (
    <div
      onClick={onClickTabItem}
      className={classNames(styles["tab-item-wrapper"], { [styles["checked"]]: checked })}
    >
      {children}
    </div>
  );
};

export default TabItem;
