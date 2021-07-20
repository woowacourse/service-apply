import React from "react";
import ApplicationFormItem from "./ApplicationFormItem";

export default {
  title: "ApplicationFormItem",
  component: ApplicationFormItem,
};

const Template = (args) => <ApplicationFormItem {...args} />;

export const Default = Template.bind({});

Default.args = {
  recruitment: {
    id: "1",
    status: "RECRUITING",
  },
  submitted: false,
};
