import { useEffect } from "react";
import { useLocation } from "react-router-dom";

type ScrollToTopProps = {
  children: JSX.Element;
};

const ScrollToTop = ({ children }: ScrollToTopProps) => {
  const { pathname } = useLocation();

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [pathname]);

  return children;
};

export default ScrollToTop;
