import styles from "./IconButton.module.css";

export type IconButtonProps = React.ButtonHTMLAttributes<HTMLButtonElement> & {
  src: string;
  alt: string;
};

const IconButton = ({ className, src, alt, ...props }: IconButtonProps) => {
  return (
    <button className={styles.button} {...props}>
      <img src={src} alt={alt} className={className} />
    </button>
  );
};

export default IconButton;
