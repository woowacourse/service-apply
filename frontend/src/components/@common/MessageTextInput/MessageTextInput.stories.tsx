import { ComponentMeta, ComponentStory } from "@storybook/react";
import { useState } from "react";
import MessageTextInput, { MessageTextInputProps } from "./MessageTextInput";

export default {
  title: "form/MessageTextInput",
  component: MessageTextInput,
} as ComponentMeta<typeof MessageTextInput>;

const Template: ComponentStory<typeof MessageTextInput> = (
  args: Omit<MessageTextInputProps, "onChange">
) => {
  const [value, setValue] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    return setValue(e.target.value);
  };

  return <MessageTextInput value={value} onChange={handleChange} {...args} />;
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
