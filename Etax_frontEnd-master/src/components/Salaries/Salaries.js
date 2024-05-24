import React from 'react'
import salaryCard from '../../assets/request/Group349.png'
import ButtonHome from '../ButtonHome/ButtonHome';
const Salaries = () => {























  
  return (
    <div className="salaries p-5">
      <div className="containerr">
        <div className="row justify-content-between ">
          <div className="col-lg-6 col-md-6 col-sm-12">
            <div className="content-salary-form">
              <div className="content-salary">
                <h5>المرتبات و الاجور</h5>
                <p>
                  خدمة الإستعلام عن المرتبات و الأجور لدى الموطن من خلال
                  منظومتنا المعتمدة من الضرائب المصرية
                </p>
              </div>
              <div className="form-salary">
                <div className="row">
                  <div className="col-lg-6 col-md-12 col-sm-12">
                    <div>
                      <label for="cardTax" class="form-label">
                        رقم البطاقه الضريبية
                      </label>
                      <input
                        type="text"
                        id="cardTax"
                        name="cardTax"
                        className="form-control mt-1 mb-1"
                        aria-describedby="passwordHelpBlock"
                        value={7826565656526}
                      />
                    </div>
                    <div>
                      <label for="valueAdd" class="form-label">
                        مسجل قيمة مضافة؟
                      </label>
                      <select
                        className="form-select mt-1 mb-1"
                        aria-label="Default select example"
                      >
                        <option selected>نعم</option>
                        <option value="2">لا</option>
                      </select>
                    </div>
                    <div>
                      <label for="formFile" className="form-label">
                        مرفق قائمة تمويل الإرهاب
                      </label>
                      <input
                        className="form-control mt-1 mb-1"
                        type="file"
                        id="formFile"
                        name="formFile"
                      />
                    </div>
                  </div>
                  <div className="col-lg-6 col-md-12 col-sm-12">
                    <div>
                      <label for="companyNumbber" className="form-label">
                        السجل التجارى للشركه
                      </label>
                      <input
                        type="text"
                        id="companyNumbber"
                        className="form-control mt-1 mb-1"
                        value={7826565656526}
                      />
                    </div>
                    <div>
                      <label for="selectDestion" className="form-label">
                        ملتزم ضريبيا عن سداد الإقرار الضريبى؟
                      </label>
                      <select
                        className="form-select mt-1 mb-1"
                        aria-label="Default select example"
                      >
                        <option selected>لا</option>
                        <option value="1">نعم</option>
                      </select>
                    </div>
                    <div>
                      <label for="uploadFile" className="form-label">
                        مرفق قائمة تمويل السداد
                      </label>
                      <input
                        type="file"
                        id="uploadFileList"
                        className="form-control mt-1 mb-1"
                        name="uploadFile"
                      />
                    </div>
                  </div>
                </div>
                <div className="mt-5">
                  <ButtonHome title="إجراء الإستعلام" />
                </div>
              </div>
            </div>
          </div>
          <div className="col-lg-4 col-md-6 col-sm-12">
            <img src={salaryCard} alt="salary" />
          </div>
        </div>
      </div>
    </div>
  );
}

export default Salaries