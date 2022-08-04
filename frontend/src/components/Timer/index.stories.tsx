import Timer from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/Timer',
  component: Timer,
} as ComponentMeta<typeof Timer>;

const Template: ComponentStory<typeof Timer> = args => <Timer {...args} />;

export const TimerTemplate = Template.bind({});
TimerTemplate.args = {
  startMinute: 1,
  callback: () => console.log('끝'),
};
