import styles from "./IconButton.module.css";

export type IconButtonProps = React.ButtonHTMLAttributes<HTMLButtonElement> & {
  className?: string;
  src: string;
  alt?: string;
};

const IconButton = ({ type, className, src, alt, ...props }: IconButtonProps) => {
  return (
    <button type={type} className={styles.button} {...props}>
      <img src={src} alt={alt} className={className} />
    </button>
  );
};

export default IconButton;
