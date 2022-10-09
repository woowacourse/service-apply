import { ComponentMeta, ComponentStory } from "@storybook/react";
import BirthField from "./BirthField";

export default {
  title: "form/BirthField",
  component: BirthField,
} as ComponentMeta<typeof BirthField>;

const Template: ComponentStory<typeof BirthField> = (args) => <BirthField {...args} />;

export const Default = Template.bind({});

export const Required = Template.bind({});
Required.args = {
  required: true,
};
