import React, { useState } from "react";
import SideBar from "../../components/SideBar";
import sidestyles from "../../styles/Sidebar.module.scss";
import updatestyles from "../../styles/ArticleUpdate.module.scss";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import { useNavigate } from "react-router-dom";
import ArrowDropDownCircleIcon from "@mui/icons-material/ArrowDropDownCircle";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";

import IconButton from "@mui/material/IconButton";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";


function ArticleUpdatePage() {
    const movePage = useNavigate();
  function goMyTeamArticle() {
    movePage("/myteamarticle");
  }

  const [menu, setMenu] = useState({
    choice : "분류",
  })

  const [form, setForm] = useState({
    title: "",
    context: "",
    writer: "",
  });

  const options = ["분류", "공지", "자유"];

  const ITEM_HEIGHT = 48;

  const [anchorEl, setAnchorEl] = React.useState(null);
  const open = Boolean(anchorEl);
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  const clickOption = (option) => {
    console.log(option)
    setMenu({ ...menu, choice: option})
  }
  return (
    <div className={sidestyles["myteam-container"]}>
      <SideBar />
      <div className={sidestyles.others}>
        <div className={updatestyles["write-container"]}>
          <ArrowBackIcon onClick={goMyTeamArticle} />
          <div className={updatestyles["inner-container"]}>
            <div className={updatestyles["article-header"]}>
              <h4>{menu.choice}</h4>
              <IconButton
                aria-label="more"
                id="long-button"
                aria-controls={open ? "long-menu" : undefined}
                aria-expanded={open ? "true" : undefined}
                aria-haspopup="true"
                onClick={handleClick}
                className={updatestyles["dropdown"]}
              >
                <ArrowDropDownCircleIcon />
              </IconButton>
              <Menu
                id="long-menu"
                MenuListProps={{
                  "aria-labelledby": "long-button",
                }}
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                PaperProps={{
                  style: {
                    maxHeight: ITEM_HEIGHT * 4.5,
                    width: "10ch",
                  },
                }}
              >
                {options.map((option) => (
                  <MenuItem
                    key={option}
                    //   selected={option === "선택"}
                    onClick={() => {handleClose(); clickOption(option);}}
                  >
                    {option}
                  </MenuItem>
                ))}
              </Menu>
              <TextField
                multiline
                required
                id="title"
                value={form.title}
                variant="standard"
                placeholder=" 제목을 입력해주세요."
                fullWidth
                onChange={(e) => setForm({ ...form, title: e.target.value })}
              />
            </div>
            <p className={updatestyles["article-writer"]}>작성자 : 독린이</p>
            <textarea
              className={updatestyles["article-context"]}
              type="text"
              value={form.context}
              onChange={(e) => setForm({ ...form, context: e.target.value })}
            />
            <div className={updatestyles["save-btn-box"]}>
              <div></div>
              <Button
                className={updatestyles["article-save"]}
                variant="contained"
                color="success"
                // onClick={}
              >
                수정
              </Button>
              <div></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default ArticleUpdatePage