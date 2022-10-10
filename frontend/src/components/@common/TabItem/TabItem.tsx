import classNames from "classnames";
import styles from "./TabItem.module.css";

type TabItemProps = {
  checked: boolean;
  children: React.ReactNode;
  onClickTabItem: React.MouseEventHandler<HTMLDivElement>;
};

const TabItem = ({ checked, children, onClickTabItem }: TabItemProps) => {
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
