import { useState } from "react";
import { ComponentMeta, ComponentStory } from "@storybook/react";
import TextInput, { TextInputProps } from "./TextInput";

export default {
  title: "form/TextInput",
  component: TextInput,
} as ComponentMeta<typeof TextInput>;

const Template: ComponentStory<typeof TextInput> = (args: Omit<TextInputProps, "onChange">) => {
  const [value, setValue] = useState("");

  const handleChange: React.ChangeEventHandler<HTMLInputElement> = (e) => {
    return setValue(e.target.value);
  };

  return <TextInput value={value} onChange={handleChange} {...args} />;
};

export const Default = Template.bind({});
Default.args = {
  maxLength: 100,
};

export const ReadOnly = Template.bind({});
ReadOnly.args = {
  maxLength: 100,
  value: "읽기전용 입력창입니다.",
  readOnly: true,
};
