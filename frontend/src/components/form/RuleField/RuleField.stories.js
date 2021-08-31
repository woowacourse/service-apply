import React from "react";
import RuleField from "./RuleField";

export default {
  title: "form/RuleField",
  component: RuleField,
};

const Template = (args) => <RuleField {...args} />;

export const Default = Template.bind({});
Default.args = {
  rules: [(target) => (target < 100 ? true : "100보다 작은 수를 입력하세요.")],
  target: 123,
};
