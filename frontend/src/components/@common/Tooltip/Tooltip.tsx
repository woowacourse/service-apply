import { BiHelpCircle } from "react-icons/bi";
import styles from "./Tooltip.module.css";

type TooltipProps = {
  messages: Readonly<string[]>;
  tooltipId: string;
};

const Tooltip = ({ messages, tooltipId }: TooltipProps) => {
  const handleKeyDown: React.KeyboardEventHandler<HTMLButtonElement> = (e) => {
    if (e.key === "Escape") {
      e.currentTarget?.blur();
    }
  };

  return (
    <div className={styles["tooltip-container"]}>
      <button aria-describedby={tooltipId} className={styles["tooltip"]} onKeyDown={handleKeyDown}>
        <BiHelpCircle />
      </button>
      <div className={styles["tooltip-modal-container"]} role="tooltip" id={tooltipId}>
        <ul className={styles["tooltip-modal"]}>
          {messages.map((message, idx) => (
            <li key={idx} className={styles["tooltip-text"]}>
              {message}
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default Tooltip;
