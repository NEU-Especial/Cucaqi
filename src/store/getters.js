const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  survey: state => state.survey.question,
  user: state => state.user.user// 直接获取用户所有信息
}
export default getters
