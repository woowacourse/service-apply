import { ComponentMeta, ComponentStory } from "@storybook/react";
import Container, { CONTAINER_SIZE, ContainerProps } from "./Container";

export default {
  title: "components/Container",
  component: Container,
} as ComponentMeta<typeof Container>;

const Template: ComponentStory<typeof Container> = (args: ContainerProps) => (
  <Container {...args} />
);

export const Default = Template.bind({});

Default.args = {
  children: "내용물",
};

export const Narrow = Template.bind({});

Narrow.args = {
  size: CONTAINER_SIZE.NARROW,
  children: "내용물",
};
