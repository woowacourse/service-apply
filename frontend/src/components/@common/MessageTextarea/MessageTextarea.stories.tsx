import { useState } from "react";
import { ComponentMeta, ComponentStory } from "@storybook/react";
import MessageTextarea, { MessageTextareaProps } from "./MessageTextarea";

export default {
  title: "form/MessageTextarea",
  component: MessageTextarea,
} as ComponentMeta<typeof MessageTextarea>;

const Template: ComponentStory<typeof MessageTextarea> = (
  args: Omit<MessageTextareaProps, "onChange">
) => {
  const [value, setValue] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    return setValue(e.target.value);
  };

  return <MessageTextarea value={value} onChange={handleChange} {...args} />;
};

export const Default = Template.bind({});
Default.args = {
  label: "이름",
};

export const Required = Template.bind({});
Required.args = {
  label: "이름",
  required: true,
};

export const WithDescription = Template.bind({});
WithDescription.args = {
  label: "이름",
  description: "이름을 입력하세요.",
};

export const MaxLength = Template.bind({});
MaxLength.args = {
  label: "이름",
  maxLength: 30,
  value: "썬",
};
