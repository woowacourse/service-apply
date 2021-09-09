import { FormContext } from "../../hooks/useFormContext";
import PropTypes from "prop-types";

const FormProvider = ({ children, ...props }) => {
  return (
    <FormContext.Provider
      value={{
        ...props,
      }}
    >
      {children}
    </FormContext.Provider>
  );
};

export default FormProvider;

FormProvider.propTypes = {
  children: PropTypes.node.isRequired,
};
