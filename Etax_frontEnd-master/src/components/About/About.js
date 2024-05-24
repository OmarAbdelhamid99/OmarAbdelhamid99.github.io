import React from 'react'
import MainHeading from '../MainHeading/MainHeading';
import './About.css'
import aboutCard from '../../assets/about/Group349.png'
const About = () => {
  return (
    <>
      <div className="about text-center m-tb-120">
        <div className="containerr">
          <MainHeading
            heading={"من نحن"}
            contentHeading={"نبذة عن منظوماتنا و أهميتها"}
          />
          <div className="row justify-content-around">
            <div className="col-lg-4 col-md-12">
              <div className="card-img-about">
                <img src={aboutCard} alt="" />
              </div>
            </div>
            <div className="col-lg-7 col-md-12 card-content">
              <div className="card-content-about">
                <p>
                  في إطار الجهود الحثيثة التي تقوم بها مصلحة الضرائب المصرية
                  لتطوير وتحديث النظم المطبقة لخدمة العمل الضريبي من خلال شبكة
                  معلومات مصلحة الضرائب المصرية وأيضا مواكبة أحدث النظم
                  والتقنيات العالمية اللازمة لتطوير منظومة العمل الضريبي فقد
                  ظهرت الحاجة الملحة لاستغلال الامكانيات الكبيرة المتاحة لدى
                  المصلحة وتطوير منظومة العمل الضريبي عن طريق تقديم الاقرارات
                  الضريبية الكترونيا في إطار الجهود الحثيثة التي تقوم بها مصلحة
                  الضرائب المصرية لتطوير وتحديث النظم المطبقة لخدمة العمل
                  الضريبي من خلال شبكة معلومات مصلحة الضرائب المصرية وأيضا
                  مواكبة أحدث النظم والتقنيات العالمية اللازمة لتطوير منظومة
                  العمل الضريبي فقد ظهرت الحاجة الملحة لاستغلال الامكانيات
                  الكبيرة المتاحة لدى المصلحة وتطوير منظومة العمل الضريبي عن
                  طريق تقديم الاقرارات الضريبية الكترونيا
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default About