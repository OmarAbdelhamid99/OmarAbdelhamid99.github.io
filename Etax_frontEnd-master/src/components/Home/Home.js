import React from 'react';
import './Home.css';
import homeCard from'../../assets/home/Group58.png'
const Home = () => {
  return (
    <>
      <div className="home-page m-tb-120">
        <div className="containerr">
          <div className="row justify-content-between cards-home">
            <div className="col-lg-7 col-md-12 p-5">
              <div className="card-content-home">
                <h3 className="text-stlye-header">منظومة التحقق الضريبى</h3>
                <p>
                  من خلال هذه المنظومة يمكنك الاستعلام و التحقق من الشئون
                  الضريبية لدى الجهات الأخرى و التحقق من مستند الرواتب الخاص
                  بالمواطن للتأكد من صحتة ولا بد ان يكون مستخرج من خلال منظومة
                  رواتب الموظفين المعتمد من و زارة المالية
                </p>
                <a className="btn btn-primary bg-button btn-serv" href="#service">

                  الخدمات
                </a>
              </div>
            </div>
            <div className="col-lg-4 col-md-12">
              <div className="card-img-home">
                <img src={homeCard} alt="" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default Home