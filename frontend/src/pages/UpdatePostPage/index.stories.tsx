import { MemoryRouter as Router, Routes, Route } from 'react-router-dom';

import UpdatePostPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/UpdatePostPage',
  component: UpdatePostPage,
} as ComponentMeta<typeof UpdatePostPage>;

const Template = () => (
  <Router
    initialEntries={[
      '/post/update',
      { pathname: '/post/update', state: { id: 1, title: '수정할 글 제목', content: '수정할 글 내용', boardId: 1 } },
    ]}
  >
    <Routes>
      <Route path="/post/update" element={<UpdatePostPage />} />
    </Routes>
  </Router>
);

export const UpdatePostPageTemplate: ComponentStory<typeof UpdatePostPage> = Template.bind({});
UpdatePostPageTemplate.args = {};
