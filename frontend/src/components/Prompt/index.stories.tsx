import Prompt from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/Prompt',
  component: Prompt,
} as ComponentMeta<typeof Prompt>;

const Template: ComponentStory<typeof Prompt> = args => <Prompt {...args} />;

export const PromptTemplate = Template.bind({});
PromptTemplate.args = {
  message: '속닥속닥 앱을 설치하시겠습니까?',
  confirmText: '설치',
  cancelText: '취소',
};
