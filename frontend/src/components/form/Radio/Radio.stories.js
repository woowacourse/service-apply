import React from 'react';
import Radio from './Radio';

export default {
  title: 'form/Radio',
  component: Radio,
};

const Template = (args) => <Radio {...args} />;

export const Default = Template.bind({});
Default.args = {
  label: '라디오 버튼',
};
