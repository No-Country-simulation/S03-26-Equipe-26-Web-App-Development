import './Login.css'
import iconGoogle from '../../assets/google.png'
import logo from '../../assets/logo.png'

function Login() {
    return (
        <>
            {/* <div className='title'> */}
                {/* <h1 >SmartTrafficFlow</h1> */}
                <section className="login center">
                    <img className='login-logo' src={logo} alt="" />
                    <h2>Login</h2>
                    <button className='center'>
                        <img src={iconGoogle} alt="" />
                        <p>Conta Google</p>
                    </button>
                    {/* <div class="g-signin2" data-onsuccess="onSignIn"></div> */}
                </section>
            {/* </div> */}
        </>
    )
}

export default Login