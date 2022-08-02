import { useEffect, useState } from 'react';

import LikeButton from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/LikeButton',
  component: LikeButton,
} as ComponentMeta<typeof LikeButton>;

const Template: ComponentStory<typeof LikeButton> = args => {
  const [state, setState] = useState(false);
  const [count, setCount] = useState(3);
  const handleClick = () => {
    setState(prev => !prev);
  };
  useEffect(() => {
    setCount(prev => (state ? prev + 1 : prev - 1));
  }, [state]);

  return <LikeButton {...args} isLiked={state} onClick={handleClick} likeCount={count} />;
};

export const LikeButtonTemplate = Template.bind({});
LikeButtonTemplate.args = {};
